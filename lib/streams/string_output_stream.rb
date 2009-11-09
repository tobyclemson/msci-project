class StringOutputStream < java.io.OutputStream
  attr_accessor :contents
  
  def initialize
    self.contents = ""
  end
  
  def close; end
  def flush; end
  
  def write(*arguments)
    if arguments.size == 1
      if arguments[0].kind_of?(Fixnum)
        array = [arguments[0].chr]
      else
        array = arguments[0]
      end
    elsif arguments.size == 3
      array = arguments[0][arguments[1], arguments[2]]
    end
    
    array.each do |char|
      self.contents << char
    end
    
    return nil
  end
  
  def reset
    self.contents = ""
  end
end