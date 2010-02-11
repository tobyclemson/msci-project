module MSci
  include_package 'msci'
  module MG
    include_package 'msci.mg'  
    module Factories
      include_package 'msci.mg.factories'
    end
    module Agents
      include_package 'msci.mg.agents'
    end
  end
end

module Jung
  include_package 'edu.uci.ics.jung'
  module Graph
    include_package 'edu.uci.ics.jung.graph'
  end
end