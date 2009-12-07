class StringInputStream < java.io.InputStream
  
  def initialize
    @contents = []
  end
  
  def contents
    @contents
  end
  
  def contents=(string)
    @contents += (string.split << "\n")
  end
  
  def available; 0; end
  def close; end
  def mark(readlimit);end
  def markSupported; false; end
  
  def read(*arguments)
    if self.contents.size == 0 
      return -1
    else
      if arguments.size > 0
        if arguments[0] == nil
          raise java.lang.NullPointerException
        end
        
        if arguments.size == 3
          offset = arguments[1]
          bytes_to_read = arguments[2]
        else
          offset = 0
          bytes_to_read = arguments[0].size
        end
      
        if (
          offset < 0 || 
          bytes_to_read < 0 || 
          (offset + bytes_to_read) > arguments[0].size
        )
          raise java.lang.IndexOutOfBoundsException
        end
        
        count = 0
        bytes_to_read.times do |i|
          if self.contents.size == 0
            return count
          else
            arguments[0][offset + i] = self.contents.shift[0]
            count += 1
          end
        end
        
        return count
      else
        return self.contents.shift[0]
      end
    end
  end
  
  def reset
    raise java.io.IOException
  end
  
  def skip(n)
    count = 0
    while (byte = read) != -1 && count < n
      count += 1
    end
    return count
  end
  
end