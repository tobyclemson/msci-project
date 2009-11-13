module FactoryHelpers
  def properties_hash(specialisations={})
    defaults = {
      "type" => "standard",
      "number-of-agents" => "10",
      "agent-type" => "abstract"
    }
    properties = Java::JavaUtil::Properties.new
    defaults.merge(specialisations).each do |property, value|
      properties.set_property(property, value)
    end
    return properties
  end
end