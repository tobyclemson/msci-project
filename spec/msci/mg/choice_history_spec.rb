require File.join(File.dirname(__FILE__), '..', '..', 'spec_helper.rb')

describe MSci::MG::ChoiceHistory do
  let(:package) { MSci::MG }
  let(:klass) { package::ChoiceHistory }

  let(:choice_history) { klass.new(1) }
  
  describe "public interface" do
    it "has an as_list instance method" do
      choice_history.should respond_to(:as_list)
    end
    
    it "has an add instance method" do
      choice_history.should respond_to(:add)
    end
    
    it "has a size instance method" do
      choice_history.should respond_to(:size)
    end
  end
  
  describe "constructor" do
    describe "with an initial length argument" do
      let(:choice_history) { klass.new(2) }
          
      it "initially sets itself to the specified size" do
        choice_history.size.should == 2
      end
      
      it "sets itself to a random list of choices" do
        lists = []
        1000.times do
          choice_history = klass.new(12)
          lists << choice_history.as_list
        end
        
        lists.uniq.size.should be > 800
      end
    end
  end
  
  describe "#as_list" do
    describe "with no arguments" do
      it "returns the entire choice history as a list" do
        choice_history = klass.new(0)
        choice_history.add(package::Choice::A)
        choice_history.add(package::Choice::B)
        choice_history.add(package::Choice::B)
        
        list = Java::JavaUtil::ArrayList.new
        list.add(package::Choice::A)
        list.add(package::Choice::B)
        list.add(package::Choice::B)
                
        choice_history.as_list.should == list
      end
      
      it "returns an empty list when the history is empty" do
        choice_history = klass.new(0)
        empty_list = Java::JavaUtil::ArrayList.new
        choice_history.as_list.should == empty_list
      end
    end
    
    describe "with an integer parameter" do
      it "returns the specified number of most recent choices" do
        choice_history = klass.new(0)
        choice_history.add(package::Choice::A)
        choice_history.add(package::Choice::B)
        choice_history.add(package::Choice::A)
        choice_history.add(package::Choice::B)
        choice_history.add(package::Choice::B)
        choice_history.add(package::Choice::B)
        
        list = Java::JavaUtil::ArrayList.new
        list.add(package::Choice::A)
        list.add(package::Choice::B)
        list.add(package::Choice::B)
        list.add(package::Choice::B)
        
        choice_history.as_list(4).should == list
      end
      
      it "throws an IndexOutOfBoundsException if the supplied number is " +
        "greater than the number of choices in the history" do
        choice_history = klass.new(10)
        expect {
          choice_history.as_list(15)
        }.to raise_error(Java::JavaLang::IndexOutOfBoundsException)
      end
    end
  end

  describe "#size" do
    it "returns the number of choices in the history" do
      choice_history = klass.new(4)
      choice_history.add(package::Choice::A)
      choice_history.add(package::Choice::B)
      
      choice_history.size.should == 6
    end
    
    it "returns zero when there are no choices in the history" do
      choice_history = klass.new(0)
      choice_history.size.should == 0
    end
  end
end