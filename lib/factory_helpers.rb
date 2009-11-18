module FactoryHelpers
  def properties_hash(specialisations={})
    defaults = {
      "type" => "standard",
      "number-of-agents" => "10",
      "agent-type" => "basic",
      "history-string-length" => "2",
      "number-of-strategies-per-agent" => "2"
    }
    properties = Java::JavaUtil::Properties.new
    defaults.merge(specialisations).each do |property, value|
      properties.set_property(property, value)
    end
    return properties
  end
end