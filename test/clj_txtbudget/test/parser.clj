(ns clj-txtbudget.test.parser
   (:use clojure.test)
   (:use clj-txtbudget.parser))


(deftest test-strip-comment
   (let [expected "This is a test   "]
     (is (= expected (strip-comment "This is a test   #Foo ")))))


(deftest test-line->map
         (let [expected {:name "Test", :amount -100.00, :start-dt "10-01-2010", :recur "m"}]
           (testing "Without Comment"
              (is (= expected (line->map "Test, -100.00, 10-01-2010, m"))))
           (testing "With Comment"
              (is (= expected (line->map "Test, -100.00, 10-01-2010, m #this is a comment"))))))


(deftest test-line-seq->map-seq
   (let [expected [{:name "Test", :amount -100.00, :start-dt "10-01-2010", :recur "m"}]]
     (testing "Without comment"
       (is (= expected (line-seq->map-seq ["Test, -100.00, 10-01-2010, m"]))))
     (testing "Without comment & leading/trailing space"
       (is (= expected (line-seq->map-seq ["  Test, -100.00, 10-01-2010, m  "]))))
     (testing "With Comment"
       (is (= expected (line-seq->map-seq ["Test, -100.00, 10-01-2010, m #Comment"]))))
     (testing "Blank line"
       (is (= [] (line-seq->map-seq ["     "]))))
     (testing "Commented Line"
       (is (= [] (line-seq->map-seq ["# This is a comment"]))))
     (testing "Commented Line w/ Leading space"
       (is (= [] (line-seq->map-seq ["  # This is a comment"]))))
))
