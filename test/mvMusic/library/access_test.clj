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
(def folders [{:path "/as/asdf/asd", :mod-date 11231},
              {:path "/as/f/asd", :mod-date 112}])
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
          (add-songs! db [(last songs)])=> (just [integer?]))
    (fact "it returns id for each successfull add"
          (add-songs! db songs)
          => (just [integer? integer? integer? integer? integer?]))
    (fact "it returns error string for unsucessful adds"
          (add-songs! db (conj songs {:file "/sdf/asdf"}))
          => (just [integer? integer? integer? integer? integer? string?]))))
(facts "about add-folders!"
   (against-background 
    [(before :facts (recreate-db! db))]
    (fact "it returns true for each successfuly added folder"
         (add-folders! db folders)=> [true true])
    (fact "it returns error string for each unsuccessfuly added folder"
         (add-folders! db (conj folders {:path"/asd/asd"}))
         => (just [true true string?]))))
