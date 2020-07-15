#!/usr/bin/python3

# THIS SCRIPT REPLACES ./java_admin_frontend-bada-1.0 WITH ../build/libs/java_admin_frontend-bada-1.0
# AND OPTIONALLY UPDATES ./admin_app.zip IF THE --zip FLAG IS SET

import argparse, os, subprocess



# Create an ArgumentParser object
program_desc= "This script performs some automated updates"
parser= argparse.ArgumentParser(description=program_desc)
parser.add_argument("--zip", action='store_true')

# ==========================================================================================
# COPY
# Copy new file and replace exisiting one in new child process
# Copy command
cpy_comm= "cp --update -T " + os.path.dirname("../../build/libs/") \
            + "/java_admin_frontend-bada-1.0.jar " \
            + os.getcwd() + "/java_admin_frontend-bada-1.0.jar"

# Execute copy command
print("Running command: " + cpy_comm)
print("Copying...")
cpy_proc= subprocess.Popen(cpy_comm, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
# Wait for process to terminate and grab output and error
(proc_out, proc_err)= cpy_proc.communicate()
# Print error and output
print("Error: " + str(proc_err))

# ==========================================================================================
# ZIP
# Check if --zip is specified
if(parser.parse_args().zip):
    # If yes, make zip file
    # Zip command
    zip_comm= "zip -r admin_app.zip ./"
    # Execute zip command in new process
    print("Zipping...")
    zip_proc= subprocess.Popen(zip_comm, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    # Wait for process to terminate and pull error and output
    (proc_out, proc_err)= zip_proc.communicate()
    # Print error and output
    print("Error: " + str(proc_err))