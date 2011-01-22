require 'rake'
require 'rspec/core/rake_task'
require 'cucumber/rake/task'

RSpec::Core::RakeTask.new
Cucumber::Rake::Task.new
  
desc "Build codebase"
task :build do
  system("ant -q")
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