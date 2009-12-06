class Experimentalist
  
  def initialize
    @measurement_results = {}
    @measurement_procs = {}
  end
  
  def add_measurement(name_of_measurement, &proc)
    raise ArgumentError, "No block supplied" unless block_given?
    raise ArgumentError, "Incorrect block arity" unless proc.arity == 1
    @measurement_procs[name_of_measurement] = proc
    @measurement_results[name_of_measurement] = []
  end
  
  def make_measurements(object_to_measure)
    @measurement_procs.each do |name_of_measurement, proc|
      result = proc.call(object_to_measure)
      @measurement_results[name_of_measurement] << result if result
    end
  end
  
  def measurement_results(name_of_measurement)
    results = @measurement_results[name_of_measurement]
    if results.size == 1
      return results.first
    else
      return results
    end
  end
end