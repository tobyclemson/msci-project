class Experimentalist
  
  def initialize
    @measurement_procs = []
  end
  
  def add_measurement(&proc)
    raise ArgumentError, "No block supplied" unless block_given?
    raise ArgumentError, "Incorrect block arity" unless proc.arity == 1
    @measurement_procs << proc
  end
  
  def make_measurements(object_to_measure)
    @measurement_procs.each do |proc|
      proc.call(object_to_measure)
    end
  end

end