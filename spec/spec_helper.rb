$LOAD_PATH.unshift File.expand_path(
  File.join(
    File.dirname(__FILE__), '..', 'dist'
  )
) unless
  $LOAD_PATH.include?(File.join(File.dirname(__FILE__), '..', 'dist')) ||
  $LOAD_PATH.include?(File.expand_path(
    File.join(File.dirname(__FILE__), '..', 'dist')
  )
)

$LOAD_PATH.unshift File.expand_path(
  File.join(
    File.dirname(__FILE__), '..', 'lib'
  )
) unless
  $LOAD_PATH.include?(File.join(File.dirname(__FILE__), '..', 'lib')) ||
  $LOAD_PATH.include?(File.expand_path(
    File.join(File.dirname(__FILE__), '..', 'lib')
  )
)

require 'rubygems'
require 'java'
require 'spec'

require 'msci_project_code.jar'

require 'ruby_mappings'
require 'streams'