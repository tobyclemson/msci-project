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
require 'active_support/all'
require 'tools'

Tools.append_to_load_path('dist')
Tools.append_to_load_path('vendor')

require 'msci-project-code.jar'
require 'mockito-1.8.2/mockito-all-1.8.2.jar'

Dir[
  File.join(
    File.dirname(__FILE__), '..', 'vendor', 'jung-2.0.1', 'lib', '*.jar'
  )
].each { |jar| 
  require jar
}

require 'ruby_mappings'
require 'factory_helpers'

import org.mockito.Mockito