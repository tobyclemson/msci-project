require 'rake'
require 'spec/rake/spectask'
require 'cucumber/rake/task'

Spec::Rake::SpecTask.new do |t|
  t.spec_opts = File.read("spec/spec.opts").gsub("\n", ' ').split(/\s+/)
end

Cucumber::Rake::Task.new
  
desc "Build codebase"
task :build do
  system("ant clean jar -q")
end

desc "Build codebase and run specs"
task :build_and_spec do
  puts "\nBUILDING:"
  Rake::Task[:build].invoke
  puts "\nRUNNING SPECS:"
  Rake::Task[:spec].invoke
end

desc "Build codebase and run features"
task :build_and_cucumber do
  puts "\nBUILDING:"
  Rake::Task[:build].invoke
  puts "\nRUNNING FEATURES:"
  Rake::Task[:cucumber].invoke
end