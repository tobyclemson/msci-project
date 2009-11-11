$LOAD_PATH.unshift File.expand_path(
  File.join(
    File.dirname(__FILE__), '..', '..', 'lib'
  )
) unless
  $LOAD_PATH.include?(File.join(File.dirname(__FILE__), '..', '..', 'lib')) ||
  $LOAD_PATH.include?(File.expand_path(
    File.join(File.dirname(__FILE__), '..', '..', 'lib')
  )
)

require 'java'
require 'tools'

Tools.append_to_load_path('dist')
Tools.append_to_load_path('vendor')

require 'msci-project-code.jar'
require 'mockito-1.8.0/mockito-all-1.8.0.jar'

require 'ruby_mappings'
require 'streams'