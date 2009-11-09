require 'streams/string_output_stream'

JAVA_STDOUT = StringOutputStream.new
JAVA_STDIN = StringOutputStream.new

java.lang.System.set_out(java.io.PrintStream.new(JAVA_STDOUT))
java.lang.System.set_err(java.io.PrintStream.new(JAVA_STDIN))