(ns db
  (:require [clojure.string :as str]))

(defn load_cust
  []
  (let [customers (str/split-lines (slurp "cust.txt"))]
    (mapv 
     (fn [cus] {:custID (get cus 0) :name (get cus 1) :address (get cus 2) :phone (get cus 3)}) 
     (map #(str/split % #"\|") customers))))

(defn load_prod
  []
  (let [products (str/split-lines (slurp "prod.txt"))]
    (mapv 
     (fn [prod] {:prodID (get prod 0) :prod (get prod 1) :unitCost (get prod 2)}) 
     (map #(str/split % #"\|") products))))

(defn load_sales
  []
  (let [sales (str/split-lines (slurp "sales.txt"))]
    (mapv 
     (fn [s] {:saleID (get s 0) :custID (get s 1) :prodID (get s 2) :itemCount (get s 3)}) 
     (map #(str/split % #"\|") sales))))


  