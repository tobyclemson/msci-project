module FactoryHelpers
  def properties_hash(specialisations={})
    defaults = {
      "number-of-agents" => "101",
      "agent-type" => "basic",
      "agent-memory-size" => "2",
      "number-of-strategies-per-agent" => "2"
    }
    properties = Java::JavaUtil::Properties.new
    defaults.merge(specialisations).each do |property, value|
      properties.set_property(property, value)
    end
    return properties
  end
end