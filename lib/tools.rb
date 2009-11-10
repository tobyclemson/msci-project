module Tools  
  @@project_root = File.expand_path(File.join(File.dirname(__FILE__), '..'))

  def self.project_root
    @@project_root
  end

  def self.append_to_load_path(*path_relative_to_root)
    added = false
    
    extra_load_path = if path_relative_to_root.size > 1
      File.join(self.project_root, *path_relative_to_root)
    else
      File.join(self.project_root, path_relative_to_root[0])
    end
    
    unless (
      $LOAD_PATH.include?(extra_load_path) || 
      $LOAD_PATH.include?(File.join(File.dirname(__FILE__), '..'))
    )
      $LOAD_PATH.unshift extra_load_path
      added = true
    end
    
    return added
  end
end