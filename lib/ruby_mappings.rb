module MSciProject
  include_package 'ic.msciproject'
  module MinorityGame
    include_package 'ic.msciproject.minoritygame'  
    module Factories
      include_package 'ic.msciproject.minoritygame.factories'
    end
  end
end

module Jung
  include_package 'edu.uci.ics.jung'
  module Graph
    include_package 'edu.uci.ics.jung.graph'
  end
end

module Java
  module Lang
    include_package 'java.lang'
  end
  module Util
    include_package 'java.util'
  end
end