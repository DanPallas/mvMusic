(ns mvMusic.log)

(defn error
  [message & more]
  (println (str message "\n" (apply str (interpose "\n" more)))))

