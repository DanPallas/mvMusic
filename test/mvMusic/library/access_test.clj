(ns mvMusic.library.access-test 
  (:require [mvMusic.library.access :refer :all]
            [clojure.java.io :refer [as-file]]
            [midje.sweet :refer :all]
            [mvMusic.library.schema :refer [recreate-db!]]))
(def db
  {:classname "org.h2.Driver"
   :subprotocol "h2"
   :subname (.getAbsolutePath (as-file "test/resources/scratchdb"))})
(def songs 
  [{:file "/path/path1/file1", :track 1, :track-total 10, :disc-no 1, :disc-total
    2, :title "song1", :artist "the artist1", :album "A album1", :album-artist 
    "album-artist1", :year 1999, :genre "Rock", :comment "comment1", 
    :composer "composer1", :original-artist "original-artist", 
    :remixer "remixed by", :conductor "conducted by", :bmp 45, 
    :grouping "group by", :isrc "isrc?", :record-label "records", 
    :encoder "none", :lyricist "dasf sdfsdfs", :lyrics "lyrics nclob", 
    :artwork-mime "png", :artwork-data (byte-array [1 2 3 4]) , :bit-rate 128, 
    :channels 2, :encoding-type "mp3", :format "mp3 format", :sample-rate 44000, 
    :length 123, :variable-bit-rate true, :mod-date 111},
   {:file "/path/path1/file2", :track 2, :track-total 10, :disc-no 1, :disc-total 
    2, :title "song2", :artist "the artist1", :album "A album1", :album-artist 
    "album-artist1", :year 1999, :genre "Rock", :comment "comment1", 
    :composer "composer1", :original-artist "original-artist", 
    :remixer "remixed by", :conductor "conducted by", :bmp 50, 
    :grouping "group by", :isrc "isrc?", :record-label "records", 
    :encoder "none", :lyricist "dasf sdfsdfs", :lyrics "lyrics nclob", 
    :artwork-mime "png", :artwork-data (byte-array [1 2 3 4]) , :bit-rate 128, 
    :channels 2, :encoding-type "mp3", :format "mp3 format", :sample-rate 44000, 
    :length 123, :variable-bit-rate true, :mod-date 111},
   {:file "/path/path2/file1", :track 1, :track-total 12, :disc-no 1, :disc-total 
    2, :title "song1", :artist "the artist1", :album "album2", :album-artist 
    "album-artist1", :year 1999, :genre "Rock", :comment "comment1", 
    :composer "composer1", :original-artist "original-artist", 
    :remixer "remixed by", :conductor "conducted by", :bmp 50, 
    :grouping "group by", :isrc "isrc?", :record-label "records", 
    :encoder "none", :lyricist "dasf sdfsdfs", :lyrics "lyrics nclob", 
    :artwork-mime "png", :artwork-data (byte-array [1 2 3 4]) , :bit-rate 128, 
    :channels 2, :encoding-type "mp3", :format "mp3 format", :sample-rate 44000, 
    :length 123, :variable-bit-rate true, :mod-date 111},
   {:file "/path2/path1/file1", :track 1, :track-total 10, :disc-no 1, :disc-total
    2, :title "song1", :artist "artist2", :album "album1", :album-artist 
    "album-artist2", :year 1999, :genre "Rock", :comment "comment1", 
    :composer "composer1", :original-artist "original-artist", 
    :remixer "remixed by", :conductor "conducted by", :bmp 45, 
    :grouping "group by", :isrc "isrc?", :record-label "records", 
    :encoder "none", :lyricist "dasf sdfsdfs", :lyrics "lyrics nclob", 
    :artwork-mime "png", :artwork-data (byte-array [1 2 3 4]) , :bit-rate 128, 
    :channels 2, :encoding-type "mp3", :format "mp3 format", :sample-rate 44000, 
    :length 123, :variable-bit-rate true, :mod-date 111},
   {:file "/path2/path1/file2" :mod-date 232}])
(def folders [{:folder "/as/asdf/asd", :mod-date 11231},
              {:folder "/as/f/asd", :mod-date 112}])
(def calculated [{:folder "/path/path1", :filename "file1", 
                  :artist-sort "artist1", :album-sort "album1"},
                 {:folder "/path/path1", :filename "file2", 
                  :artist-sort "artist1", :album-sort "album1"},
                 {:folder "/path/path2", :filename "file2", 
                  :artist-sort "artist1", :album-sort "album2"},
                 {:folder "/path2/path1", :filename "file1", 
                  :artist-sort "artist2", :album-sort "album1"},
                 {:folder "/path2/path1", :filename "file1"}])

(facts
  "about add-songs!"
  (against-background 
    [(before :facts (recreate-db! db))]
    (fact "it returns id for each successfull add (one song)"
          (add-songs! db [(last songs)])=> (just [true]))
    (fact "it returns id for each successfull add"
          (add-songs! db songs)
          => (just [true true true true true]))
    (fact "it returns error string for unsucessful adds"
          (add-songs! db (conj songs {:file "/sdf/asdf"}))
          => (just [true true true true true string?]))
    (fact "it updates songs that already exist"
        (add-songs! db songs)
        (add-songs! db [(assoc (first songs) :title "test-title")])
        (get-songs db :file (:file (first songs))) 
        => (just (contains {:title "test-title"})))))

(facts "about add-folders!"
   (against-background 
    [(before :facts (recreate-db! db))]
    (fact "it returns true for each successfuly added folder"
         (add-folders! db folders) => [true true])
    (fact "it returns error string for each unsuccessfuly added folder"
         (add-folders! db (conj folders {:path"/asd/asd"}))
         => (just [true true string?]))
    (fact "if folder already exists, it updates it"
          (add-folders! db [{:folder "folder1" :mod-date 10}])
          (add-folders! db [{:folder "folder1" :mod-date 100}]) => [true]
          (get-folder db "folder1") => {:folder "folder1" :mod-date 100})))

(fact "about add-folder!"
      (against-background 
        [(before :facts (recreate-db! db))]
        (fact "it calls add-folders! with folder in a seq"
              (add-folder! db {:folder "/path1" :mod-date 100}) => true
              (provided 
                (add-folders! db [{:folder "/path1" :mod-date 100}])
                => [true]))))

(facts "about get-folder"
     (against-background
         [(before :facts (recreate-db! db)) 
          (before :checks (add-folders! db folders))]
         (fact "it returns nil if the db doesn't containg folder"
               (get-folder db "path1") => nil)
         (fact "it returns the folder if the path given matches a folder"
               (get-folder db (:folder (first folders))) => (first folders))))
(defn blob?
  [o]
  (instance? o org.h2.jdbc.JdbcBlob))
(defn clob?
  [o]
  (instance? o org.h2.jdbc.JdbcClob))

(facts 
  "about get-songs"
  (against-background 
    [(before :facts (recreate-db! db))]
    (fact 
      "it gets all songs in unspecified order when no options are passed"
          (add-songs! db [(first songs) (second songs)])
          (get-songs db) 
          => (just (contains  {:file "/path/path1/file1"})
                   (contains  {:file "/path/path1/file2"})))
    (fact "when :file <filename> is passed it returns that file"
          (add-songs! db songs)
          (get-songs db :file (:file (first songs)))
          => (just (contains {:file (:file (first songs))})))))
