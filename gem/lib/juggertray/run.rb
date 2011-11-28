#!/usr/bin/env ruby
$:.unshift File.join(File.dirname(__FILE__), "..", "lib")

def run
  jar_file = "#{File.dirname(__FILE__)}/juggertray.jar"
  command = "java -jar #{jar_file}"
  ARGV.each do |arg|
    command << " \"#{arg}\""
  end
  puts "#{Dir.getwd} => '#{command}'"
  if system(command) then
    exit(0)
  else
    exit(-1)
  end
end

run()


    
