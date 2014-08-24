(ns mvMusic.library.schema-test
  (:require [mvMusic.library.schema :refer :all]
            [midje.sweet :refer :all]
            [clojure.java.io :refer [as-file]]
            [mvMusic.global :refer [cfg-map]]))

(facts 
  "about get-db-spec"
  (fact "it returns a db-spec using the given path as subname"
        (:subname (get-db-spec "test/db")) 
        => (.getAbsolutePath (as-file "test/db")))
  (fact "it uses the temp-folder in cfg-map if no path is given"
        (with-redefs [cfg-map {:temp-folder "test/db"}]
          (:subname (get-db-spec)) 
          => (.getAbsolutePath (as-file "test/db/library")))))
(facts
  "about create-db! and drop-db!"
  (fact "create-db! creates a db using the db spec"
        (create-db! (get-db-spec "test/resources/testdb")) => '(0 0 0)
        (.exists (as-file "test/resources/testdb.mv.db")) => true)
  (fact "drop-db! deletes database files"
        (drop-db! (get-db-spec "test/resources/testdb")) => '(0)
        (.exists (as-file "test/resources/testdb.mv.db")) => false))
(facts
  "about get-db!"
  (fact 
    "it returns a db spec and creates a db at the location in cfg-map"
    (with-redefs [cfg-map {:temp-folder "test/db2"}] 
      (get-db!) => (get-db-spec "test/db2/library"))
    (against-background 
      (after :checks (drop-db! (get-db-spec "test/db2/library"))))))
