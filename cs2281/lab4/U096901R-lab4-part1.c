// Name: Heng Low Wee
// Email: lwheng@nus.edu.sg
// Matric: U096901R

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>

#define MAX_SIZE (128)

int main(int argc, char * argv[]) {
    char buf[MAX_SIZE];
    int pos = 0;
    char c;
    char *arguments[64];
    char *token;
    int stat = 0;
    if (argc == 1) {
        while ((c=getchar()) != EOF) {
            if (c != '\n') {
                buf[pos] = c;
                pos++;
            }
            else {
                buf[pos] = '\0';
                // Run the command
                pos = 0;
                token = strtok(buf, " ");
                while (token) {
                    arguments[pos++] = token;
                    token = strtok(NULL, " ");
                }
                arguments[pos] = NULL;
                int pid = fork();
                if (pid == 0) {
                    // Child
                    stat = execve(arguments[0], arguments, NULL);
                    exit(stat);
                }
                else if (pid == -1) {
                    // Failed to fork
                    exit(1);
                }
                else {
                    wait(&stat);
                }
                pos = 0;
            }
        }
    }
    else {
        // argument provided. read the file line by line
        FILE *fp;
        fp = fopen(argv[1], "rt");
        while ( fgets (buf, MAX_SIZE, fp) != NULL) {
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

                // We set up arguments already, now we execute it
                // First we have to fork()
                int pid = fork();
                if (pid == 0) {
                    // Child
                    stat = execve(arguments[0], arguments, NULL);
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
                }
            }
        }
    }
    return 0;
}
