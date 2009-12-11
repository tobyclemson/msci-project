require 'streams/string_output_stream'
require 'streams/string_input_stream'

JAVA_STDOUT = StringOutputStream.new
JAVA_STDERR = StringOutputStream.new
JAVA_STDIN  = StringInputStream.new

#java.lang.System.set_out(java.io.PrintStream.new(JAVA_STDOUT))
#java.lang.System.set_err(java.io.PrintStream.new(JAVA_STDERR))
#java.lang.System.set_in(JAVA_STDIN)