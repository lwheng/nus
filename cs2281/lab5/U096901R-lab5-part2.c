// Name: Heng Low Wee
// Email: lwheng@nus.edu.sg
// Matric: U096901R

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <procfs.h>
#include <dirent.h>
#include <pwd.h>

int col_pid = 0;
int col_uid = 0;
int col_n = 0;
char *shells[64];
int noOfShells = 0;
int UIDs[1024];
int noOfUID = 0;

int setup(int argc, char * argv[]) {
    char c;
    while ((c=getopt(argc, argv, "pnu")) != -1) {
        switch (c) {
            case 'p':
                col_pid = 1;
                break;
            case 'u':
                col_uid = 1;
                break;
            case 'n':
                col_n = 1;
                break;
            default:
                abort();
        }
    }
    if (col_uid == 1) {
        col_pid = 0;
        col_n = 0;
    }
    return 0;
}

int checkIsLogin(char *input) {
    int i;
    for (i=0; i<noOfShells; i++) {
        if (strcmp(input, shells[i]) == 0) {
            return 0;
        }
    }
    return 1;
}

int checkIfNew(int input) {
    int i;
    for (i=0; i<noOfUID; i++) {
        if (input == UIDs[i]) {
            return 1;
        }
    }
    return 0;
}

int main (int argc, char * argv[]) {
    setup(argc, argv);
    DIR *d;
    struct dirent *dp;
    char pid_file[128];
    FILE *fp;
    struct passwd *user_p;
    time_t theTime;
    psinfo_t psinfo_buf;
    char *token;

    char *shell_def = getenv("MYSHELLS");
    if (shell_def == NULL) {
        shell_def = getenv("SHELL");
    }
    token = strtok(shell_def, ":");
    while (token) {
        shells[noOfShells] = token;
        noOfShells++;
        token = strtok(NULL, ":");
    }

    if ((d=opendir("/proc")) == NULL) {
        fprintf(stderr, "Cannot open %s!\n", "/proc");
        return 1;
    }

    if (col_uid == 1) {
        printf("%8s\n", "UID");
    }
    else {
        if (col_pid == 1) {
            printf("%8s %5s %16s %.24s\n", "UID", "PID", "FNAME", "STIME");
        }
        else {
            printf("%8s %16s %.24s\n", "UID", "FNAME", "STIME");
        }
    }
    while ((dp=readdir(d)) != NULL) {
        if (dp->d_name[0] != '.') {
            sprintf(pid_file, "/proc/%s/psinfo", dp->d_name);
            fp = fopen(pid_file, "r");
            if (fp != NULL) {
                fread(&psinfo_buf, sizeof(psinfo_t), 1, fp);
                user_p = getpwuid(psinfo_buf.pr_uid);
                theTime = (time_t) psinfo_buf.pr_start.tv_sec;
                // Filter for only login shells
                // List of shells are captured in shells[]. might need
                // to tokenize the elements
                if (checkIsLogin(psinfo_buf.pr_fname) == 0) {
                    if (col_uid == 1) {
                        // print only UID
                        if (checkIfNew(psinfo_buf.pr_uid) == 0) {
                            printf("%8s\n", user_p->pw_name);
                            UIDs[noOfUID] = psinfo_buf.pr_uid;
                            noOfUID++;
                        }
                    }
                    else {
                        // print UID and FNAME and STIME
                        if (col_pid == 1) {
                            // print PID too
                            printf("%8s %5d %16s %.24s\n", user_p->pw_name, (int)psinfo_buf.pr_pid, psinfo_buf.pr_fname, ctime(&theTime));
                        }
                        else {
                            printf("%8s %16s %.24s\n", user_p->pw_name, psinfo_buf.pr_fname, ctime(&theTime));
                        }
                    }
                }
            }
        }
    }
    return 0;
}
