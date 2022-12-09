(ns app
  (:gen-class)
  (:require [db])
  (:require [menu]))

(defn customer_table
  [cust]
  (println)
  (println "****************** Customers ****************** \n")
  (doall 
   (map 
    (fn [cust] 
      (printf "%s:[\"%s\" \"%s\" \"%s\"] \n" 
              (:custID cust) 
              (:name cust) 
              (:address cust) 
              (:phone cust))) 
    cust)))

(defn product_table
  [prod]
  (println)
  (println "**** Products **** \n")
  (doall 
   (map 
    (fn [prod] 
      (printf "%s:[\"%s\" %s] \n" 
              (:prodID prod) 
              (:prod prod) 
              (:unitCost prod))) 
    prod)))

(defn sales_table
  [cust prod sales]
  (mapv (fn [sl]
          {:saleID (:saleID sl)
           :name (:name (first (filter (fn [cust] (= (:custID cust) (:custID sl))) cust)))
           :prod (:prod (first (filter (fn [prod] (= (:prodID prod) (:prodID sl))) prod)))
           :itemCount (:itemCount sl)}) sales))

(defn print_sales
  [cust prod sales]
  (println)
  (println "********** Sales ********** \n")
  (let [sales_tb (sales_table cust prod sales)]
    (doall
     (map
      (fn [sale]
        (printf "%s:[\"%s\" \"%s\" %s] \n"
                (:saleID sale) (:name sale)
                (:prod sale)
                (:itemCount sale)))
      sales_tb))))

(defn total_sales
  [cust prod sales]
  (println)
  (print "Enter the customer name: ")
  (flush)
  (let [customer (read-line)
        sales_tb (sales_table cust prod sales)
        items_count (mapv #(read-string %) 
                    (mapv (fn [x] (:itemCount x)) (filter #(= (:name %) customer) sales_tb)))
        items_price (mapv #(read-string %) 
                     (remove nil? (mapv (fn [record] (:unitCost
                       (first (filter (fn [prod] (and
                            (= (:prod prod) (:prod record))
                            (= (:name record) customer)))
                         prod))))
                    sales_tb)))
        total (reduce + (mapv * items_price items_count))]
    (printf "%s: $" customer) (print total)))

(defn prod_count
  [cust prod sales]
  (println)
  (print "Enter the product: ")
  (flush)
  
  (let [item (read-line)
        sales_tb (sales_table cust prod sales)
        item_count (reduce + (mapv #(read-string %) 
                                   (mapv 
                                    (fn [x] (:itemCount x)) 
                                    (filter #(= (:prod %) item) sales_tb))))]
    (printf "%s: " item) (print item_count)))

(defn program 
  ([cust prod sales option] 
   (if (< option 6)
     (do 
       (cond 
         (== option 1) (customer_table cust) 
         (== option 2) (product_table prod)
         (== option 3) (print_sales cust prod sales)
         (== option 4) (total_sales cust prod sales)
         (== option 5) (prod_count cust prod sales)
         )
       (let [newOption (menu/-menu)]
         (program cust prod sales newOption)))
     (println "Goodbye!")))
  
  ([cust prod sales]
  (let [option (menu/-menu)]
    (program cust prod sales option))))


(defn -main
  []
  (let [cust (db/load_cust)
        prod (db/load_prod)
        sales (db/load_sales)]
    (program cust prod sales)))

(-main)