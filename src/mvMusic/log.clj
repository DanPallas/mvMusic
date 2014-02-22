(ns mvMusic.log)

(defn error
  [x & more]
  (println (str x "\n" (apply str (interpose "\n" more)))))

