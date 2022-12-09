(ns menu)

(defn -menu
  []
  (print "\n
            ****** Sales Menu ****** 
            ------------------------ 
            1. Display Customer Table
            2. Display Product Table
            3. Display Sales Table
            4. Total Sales for Customer
            5. Total Count for Product
            6. Exits
            
            Enter an Option: ") 
  (flush)
  (Integer/parseInt (read-line)))