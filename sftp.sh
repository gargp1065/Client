#!/bin/bash

# MySQL Database Details
#DB_USER="root"
#DB_PASSWORD="Ro)t@db@123"
#DB_HOST="127.0.0.1"
#DB_NAME="app"
#DB_TABLE="your_db_table"

# Directory to monitor
flag=$1
MONITOR_DIR=$2
MOVE_DIR=$MONITOR_DIR"/sent_to_eirs"
LOG_FILE=$3
# SFTP Details
SFTP_USER="eirapp"
SFTP_PASS="Eir@pp23"
SFTP_PORT="22024"
#DESTINATION_SERVERS=("10.100.2.8" "dest_server_2" "dest_server_3")
DESTINATION_SERVERS=("10.100.2.7")
DESTINATION_DIR=$4

# grep keywords.....
GREP_KEYWORD_1=$5
GREP_KEYWORD_2=$6
# Function to update MySQL table
function update_mysql_table {
    mysql -u "$DB_USER" -p"$DB_PASSWORD" -h "$DB_HOST" -D "$DB_NAME" <<EOF
    INSERT INTO $DB_TABLE (start_time, end_time, filename, rows_count, destination_ip, status)
    VALUES ('$1', '$2', '$3', '$4', '$5', '$6');
EOF
}



function pushFiles {
lftp -u "$SFTP_USER","$SFTP_PASS" "sftp://$1:$SFTP_PORT" -e "
set ftp:ssl-allow no; \
set xfer:log-file '$LOG_FILE"/sfp_"$1"_"$2".log"'; \
set xfer:log true; \
lcd '$MONITOR_DIR'; \
cd '$DESTINATION_DIR'; \
put $2; \
bye"
}

function moveFile {
        mv $1 $MOVE_DIR
        echo "File [$1] Moved to ./sent_to_eirs"
}

SUB='bytes transferred'

blank=""
# Start monitoring the directory for file creation
    cd $MONITOR_DIR
    start_time=$(date +"%Y-%m-%d %H:%M:%S")
    #file_to_ftp=`ls -ltrt *$GREP_KEYWORD_1  | grep $GREP_KEYWORD_2| awk '{print$9}'`
    for file_to_ftp in `ls -ltrt *$GREP_KEYWORD_1  | grep $GREP_KEYWORD_2| awk '{print$9}'`
    do
    openStatus=`lsof -u admin | grep $file_to_ftp | awk '{print $2}'`
    if [ "$openStatus" == "$blank" ]
    then
        echo "file [$file_to_ftp] is not open"
        # Count the number of rows in the file (assuming it's a text file)
        #rows_count=$(wc -l < "$filepath")
        # Perform SFTP transfer to multiple destinations
        for server in "${DESTINATION_SERVERS[@]}"; do
          sftpStatus=`pushFiles "$server" "$file_to_ftp"`
          echo "sftp status = "$sftpStatus
          if [ [ "$flag" == "1" ] && [ "$sftpStatus" == *"$SUB"* ] ];
          then
                  moveFile "$file_to_ftp"
          fi
        done

        # Log end time, filename, rows count, destination IPs, and status in MySQL table
        end_time=$(date +"%Y-%m-%d %H:%M:%S")

    else
            echo "cant ftp, file [$file_to_ftp] is open"
    fi
        done

    # Log end time, filename, rows count, destination IPs, and status in MySQL table
#    end_time=$(date +"%Y-%m-%d %H:%M:%S")
#    for server in "${DESTINATION_SERVERS[@]}"; do
#        update_mysql_table "$start_time" "$end_time" "$file" "$rows_count" "$server" "successful"
#    done
