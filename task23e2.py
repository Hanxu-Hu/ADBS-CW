import os
import traceback
import shutil

minibase = r"Minibase/target/minibase-1.0.0-jar-with-dependencies.jar"
test_dir = r"Minibase/data_1/evaluation_extra/extra_02/"

db_dir = os.path.join(test_dir, "db")
input_dir = os.path.join(test_dir, "input")
output_dir = os.path.join(test_dir, "output")
exp_output_dir = os.path.join(test_dir, "expected_output")

try:
    shutil.rmtree(output_dir)
except Exception:
    traceback.print_exc()
try:
    os.makedirs(output_dir)
except OSError:
    traceback.print_exc()
    
input_files = [f for f in os.listdir(input_dir) if os.path.isfile(os.path.join(input_dir, f)) and f[-4:] == ".txt"]

for input_file_base in input_files:
    output_filename = input_file_base[:-4] + ".csv"
    output_file = os.path.join(output_dir, output_filename)
    exp_output_file = os.path.join(exp_output_dir, output_filename)
    input_file = os.path.join(input_dir, input_file_base)
    cmd = "java -cp {} ed.inf.adbs.minibase.Minibase {} {}  {}".format(minibase, db_dir, input_file, output_file)
    print(cmd)
    print("Execute on " + input_file_base)
    os.system(cmd)
    with open(output_file, 'r') as f:
        ref1_output_content = f.readlines()
        ref1_output_content.sort()
        ref1_output_content = list(map(lambda x: x.rstrip(), ref1_output_content))
    with open(exp_output_file, 'r') as f:
        ref2_output_content = f.readlines()
        ref2_output_content.sort()
        ref2_output_content = list(map(lambda x: x.rstrip(), ref2_output_content))
    print(ref1_output_content)
    print(ref2_output_content)
    if ref1_output_content != ref2_output_content:
        raise Exception("Not match")


#try:
#    shutil.rmtree(ref1_output_dir)
#except Exception:
#    traceback.print_exc()
#try:
#    shutil.rmtree(ref2_output_dir)
#except:
#    traceback.print_exc()
