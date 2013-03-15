// Name: Heng Low Wee
// Email: lwheng@nus.edu.sg
// Matric: U096901R

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>

#define LINE_SIZE (128)
#define ARGFILE_SIZE (512)

int ignoreFlag = 0;
int verboseFlag = 0;
int errorFlag = 0;
char *argfile = "";
char *scriptfile = "";
char buf[LINE_SIZE];
char argfilebuf[ARGFILE_SIZE];
char *argfileargs[ARGFILE_SIZE/2];
int argcount=0;

int setup(int argc, char * argv[]) {
    char c;
    while ((c=getopt(argc,argv, "iva:")) != -1) {
        switch (c) {
            case 'i':
                ignoreFlag = 1;
                break;
            case 'v':
                verboseFlag = 1;
                break;
            case 'a':
                argfile = optarg;
                // Read argfile into memory
                FILE *fp;
                fp = fopen(argfile, "r");
                while (fscanf(fp, "%s", argfilebuf) != -1) {
                    argfileargs[argcount] = malloc(strlen(argfilebuf));
                    strcpy(argfileargs[argcount], argfilebuf);
                    argcount++;
                }
                break;
            case '?':
                if (optopt == 'a')
                    fprintf (stderr, "Option -%c requires an argument.\n", optopt);
                else if (isprint (optopt))
                    fprintf (stderr, "Unknown option `-%c'.\n", optopt);
                else
                    fprintf (stderr,"Unknown option character `\\x%x'.\n",optopt);
                exit(1);
            default:
                abort();
        }
    }
    int i;
    for (i = optind; i < argc; i++)
        scriptfile = argv[i];

}

void print_arguments(char *argv[]) {
    int i=0;
    while (argv[i]!=NULL) {
        fprintf(stderr, "%s ", argv[i]);
        i++;
    }
    fprintf(stderr, "\n", NULL);
}

char ** copy_arguments(char *head[], char *tail[], int x, int y) {
    char **temp = malloc(sizeof(head) + sizeof(tail));
    int index = 0;
    while (index<x) {
        temp[index] = head[index];
        index++;
    }
    int secondindex = 0;
    while (secondindex<y) {
        temp[index] = tail[secondindex];
        index++;
        secondindex++;
    }
    return temp;
}

int main(int argc, char * argv[]) {
    int pos = 0;
    char c;
    char *arguments[64];
    char *token;
    int stat = 0;
    int i=0;
    int pid;
    FILE *fp;

    setup(argc, argv);

    if ((int)strlen(scriptfile) == 0) {
        // No script file provided
        pos = 0;
        while ((c=getchar())!=EOF) {
            if (c!='\n') {
                buf[pos] = c;
                pos++;
            }
            else {
                buf[pos] = '\0';
                pos = 0;
                // Run command
                token = strtok(buf, " ");
                while (token) {
                    arguments[pos++] = token;
                    token = strtok(NULL, " ");
                }
                arguments[pos] = NULL;
                char **allargs = copy_arguments(arguments, argfileargs, pos, argcount);
                if (verboseFlag == 1) {
                    print_arguments((argcount!=0) ? allargs : arguments);
                }
                pid = fork();
                if (pid == 0) {
                    stat = (argcount!=0) ? execvp(allargs[0], allargs) : execvp(arguments[0], arguments);
                    exit(stat);
                }
                else if (pid == -1) {
                    exit(1);
                }
                else {
                    wait(&stat);
                    if (stat != 0) {
                        // ERROR!
                        if (ignoreFlag != 1) {
                            fprintf(stderr, "Error running '%s'. Ignore mode OFF. Exiting.\n", buf);
                            exit(stat);
                        }
                    }
                }
                pos = 0;
            }
        }
    }
    else {
        // script file provided
        fp = fopen(scriptfile,"r");
        while (fgets(buf, LINE_SIZE, fp) !=NULL) {
            if (buf[strlen(buf)-1] == '\n') buf[strlen(buf)-1] = '\0';
            if (buf[0] != '#') {
                // buf is now the command in a string
                // we tokenize it into the command and its arguments
                token = strtok(buf, " ");
                pos = 0;
                while (token) {
                    arguments[pos++] = token;
                    token = strtok(NULL, " ");
                }
                arguments[pos] = NULL;
                char **allargs = copy_arguments(arguments, argfileargs, pos, argcount);
                if (verboseFlag == 1) {
                    print_arguments((argcount!=0) ? allargs : arguments);
                }
                // We set up arguments already, now we execute it
                // First we have to fork()
                int pid = fork();
                if (pid == 0) {
                    stat = (argcount!=0) ? execvp(allargs[0], allargs) : execvp(arguments[0], arguments);
                    exit(stat);
                }
                else if (pid == -1) {
                    // Failed to fork
                    exit(1);
                }
                else {
                    // Parent. As the parent, we have to wait for
                    // child process to complete
                    wait(&stat);
                    if (stat != 0) {
                        // ERROR!
                        if (ignoreFlag != 1) {
                            fprintf(stderr, "Error running '%s'. Ignore mode OFF. Exiting.\n", buf);
                            exit(1);
                        }
                    }
                }
            }
        }
    }
    return 0;
}
