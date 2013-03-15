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

int main (int argc, char * argv[]) {
    DIR *d;
    struct dirent *dp;
    char *pid_file;
    FILE *fp;
    struct passwd *user_p;
    time_t theTime;
    psinfo_t psinfo_buf;
    if ((d=opendir("/proc")) == NULL) {
        fprintf(stderr, "Cannot open %s!\n", "/proc");
        return 1;
    }
    printf("%8s %5s %16s %.24s\n", "UID", "PID", "FNAME", "STIME");
    while ((dp=readdir(d)) != NULL) {
        if (dp->d_name[0] != '.') {
            sprintf(pid_file, "/proc/%s/psinfo", dp->d_name);
            fp = fopen(pid_file, "r");
            if (fp != NULL) {
                fread(&psinfo_buf, sizeof(psinfo_t), 1, fp);
                user_p = getpwuid(psinfo_buf.pr_uid);
                theTime = (time_t) psinfo_buf.pr_start.tv_sec;
                printf("%8s %5d %16s %.24s\n", user_p->pw_name,
                        psinfo_buf.pr_pid, psinfo_buf.pr_fname,
                        ctime(&theTime));
            }
        }
    }
    return 0;
}
