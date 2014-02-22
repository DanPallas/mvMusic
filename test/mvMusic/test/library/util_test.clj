(ns mvMusic.test.library.util-test
  (:use [midje.sweet]
        [mvMusic.library.util])
  (:require [mvMusic.library.fetch :as f]))

(facts 
  "about albums=?"
  (fact 
    "when given two equivalent albums, it returns true"
    (albums=? {:id 1, :title "album1", :artist "art1", :image "1", :sort "1"}
              {:id 2, :title "album1", :artist "art1", :image "2", :sort "1"})
    => true)
  (fact 
    "when given more than two equivalent albums, it returns true"
    (albums=? {:id 1, :title "album1", :artist "art1", :image "1", :sort "1"}
              {:id 2, :title "album1", :artist "art1", :image "2", :sort "1"}
              {:id 2, :title "album1", :artist "art1", :image "2", :sort "1"} )
    => true)
  (fact 
    "when given more than two inequivalent albums, it returns false"
    (albums=? {:id 1, :title "album1", :artist "art1", :image "1", :sort "1"}
              {:id 2, :title "album3", :artist "art1", :image "2", :sort "1"}
              {:id 2, :title "album1", :artist "art1", :image "2", :sort "1"} )
    => false)
  (fact 
    "when given two inequivalent albums, it returns false"
    (albums=? {:id 1, :title "album1", :artist "art1", :image "1", :sort "1"}
              {:id 2, :title "album3", :artist "art1", :image "2", :sort "1"})
    => false))

(facts 
  "about extract-albums"
  (fact "When given a single song, a collection containing a map of it's album should be returned"
        (extract-albums {:track 1, :disc 1, :title "song-title", :artist "art",
                         :lenght 134212, :year 1999, :extension "mp3",
                         :genre "rock", :folder "/music/sfa/",
                         :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
                         :mod-date 2313, :album-title "album1",
                         :album-artist "art", :image "/asfd/asdf.jpg", 
                         :album-sort "album1-sort"}) 
        => [{:title "album1", :artist "art",
            :image "/asfd/asdf.jpg", 
            :sort "album1-sort"}])
  (fact "should return a collection containing map of single album when passed songs from the same album"
        (extract-albums [{:track 1, :disc 1, :title "song-title", :artist "art",
                         :lenght 134212, :year 1999, :extension "mp3",
                         :genre "rock", :folder "/music/sfa/",
                         :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
                         :mod-date 2313, :album-title "album1",
                         :album-artist "album-artist", :image "/asfd/asdf.jpg", 
                         :album-sort "album1-sort"}
                        {:track 2, :disc 1, :title "song-title2", :artist "art",
                         :lenght 134212, :year 1999, :extension "mp3",
                         :genre "rock", :folder "/music/sfa/",
                         :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
                         :mod-date 2313, :album-title "album1",
                         :album-artist "album-artist", :image "/asfd/asdf.jpg", 
                         :album-sort "album1-sort"}])
                        => [{:title "album1", :artist "album-artist",
                             :image "/asfd/asdf.jpg", 
                             :sort "album1-sort"}])
  (fact "should return a collection containing maps of albums when passed songs from the different albums (title)"
        (extract-albums [{:track 1, :disc 1, :title "song-title", :artist "art",
                         :lenght 134212, :year 1999, :extension "mp3",
                         :genre "rock", :folder "/music/sfa/",
                         :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
                         :mod-date 2313, :album-title "album1",
                         :album-artist "art", :image "/asfd/asdf.jpg", 
                         :album-sort "album1-sort"}
                        {:track 1, :disc 1, :title "song-title2", :artist "art",
                         :lenght 134212, :year 1999, :extension "mp3",
                         :genre "rock", :folder "/music/sfa/",
                         :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
                         :mod-date 2313, :album-title "album2",
                         :album-artist "art", :image "/asfd/asdf.jpg", 
                         :album-sort "album1-sort"}])
                        => (just [{:title "album1",
                             :artist "art", :image "/asfd/asdf.jpg", 
                             :sort "album1-sort"}
                            {:title "album2",
                             :artist "art", :image "/asfd/asdf.jpg", 
                             :sort "album1-sort"}] :in-any-order))
  (fact "should return a col containing maps of albums when passed songs from the different albums (sort)"
        (extract-albums [{:track 1, :disc 1, :title "song-title", :artist "art",
                         :lenght 134212, :year 1999, :extension "mp3",
                         :genre "rock", :folder "/music/sfa/",
                         :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
                         :mod-date 2313, :album-title "album1",
                         :album-artist "art", :image "/asfd/asdf.jpg", 
                         :album-sort "album1-sort"}
                        {:track 1, :disc 1, :title "song-title2", :artist "art",
                         :lenght 134212, :year 1999, :extension "mp3",
                         :genre "rock", :folder "/music/sfa/",
                         :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
                         :mod-date 2313, :album-title "album1",
                         :album-artist "art", :image "/asfd/asdf.jpg", 
                         :album-sort "album1-sort2"}])
                        => (just [{:title "album1",
                             :artist "art", :image "/asfd/asdf.jpg", 
                             :sort "album1-sort"}
                            {:title "album1",
                             :artist "art", :image "/asfd/asdf.jpg", 
                             :sort "album1-sort2"}] :in-any-order))
  (fact "should return a coll of a single album when called with a single song without any album art. "
        (extract-albums [{:track 1, :disc 1, :title "song-title", :artist "art",
                         :lenght 134212, :year 1999, :extension "mp3",
                         :genre "rock", :folder "/music/sfa/",
                         :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
                         :mod-date 2313, :album-title "album1",
                         :album-artist "art", 
                         :album-sort "album1-sort"} ])
                        => [{:title "album1",
                             :artist "art", :image nil, 
                             :sort "album1-sort"}])
  (fact "should return a col containing a map of an album when passed songs from the same album with different images (will use last album art)"
        (extract-albums [{:track 1, :disc 1, :title "song-title", :artist "art",
                         :lenght 134212, :year 1999, :extension "mp3",
                         :genre "rock", :folder "/music/sfa/",
                         :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
                         :mod-date 2313, :album-title "album1",
                         :album-artist "art", :image "/asfd/asdf.jpg", 
                         :album-sort "album1-sort"}
                        {:track 1, :disc 1, :title "song-title", :artist "art",
                         :lenght 134212, :year 1999, :extension "mp3",
                         :genre "rock", :folder "/music/sfa/",
                         :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
                         :mod-date 2313, :album-title "album1",
                         :album-artist "art", :image "/asfd/asdf222.jpg", 
                         :album-sort "album1-sort"}])
                        => [{:title "album1",
                             :artist "art", :image "/asfd/asdf222.jpg", 
                             :sort "album1-sort"}]))

(def album1 {:id 2
             :title "album1",
             :artist "art", 
             :sort "album1-sort"
             :image nil})
(def raw-song1 {:track 1, :disc 1, :title "song-title", :artist "art",
            :lenght 134212, :year 1999, :extension "mp3",
            :genre "rock", :folder "/music/sfa/",
            :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
            :mod-date 2313, :album-title "album1",
            :album-artist "art", 
            :album-sort "album1-sort"})
(def song1 {:track 1, :disc 1, :title "song-title", :artist "art",
            :lenght 134212, :year 1999, :extension "mp3",
            :genre "rock", :folder "/music/sfa/",
            :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
            :mod-date 2313, :album-id 1})
(def raw-song2 {:track 2, :disc 1, :title "song-title", :artist "art",
            :lenght 134212, :year 1999, :extension "mp3",
            :genre "rock", :folder "/music/sfa/",
            :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
            :mod-date 2313, :album-title "album1",
            :album-artist "art", 
            :album-sort "album1-sort"})
(def song2 {:track 2, :disc 1, :title "song-title2", :artist "art",
            :lenght 134212, :year 1999, :extension "mp3",
            :genre "rock", :folder "/music/sfa/",
            :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
            :mod-date 2313, :album-id 1})
(def album2 {:id 2,
             :title "album2",
             :artist "art2", 
             :sort "album2-sort",
             :image nil})
(def raw-song3 {:track 1, :disc 1, :title "song-title2", :artist "art2",
                :lenght 134212, :year 1999, :extension "mp3",
                :genre "rock", :folder "/music/sfa/",
                :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
                :mod-date 2313, :album-title "album2",
                :album-artist "art2", 
                :album-sort "album2-sort"})
(def song3 {:track 1, :disc 1, :title "song-title2", :artist "art2",
                :lenght 134212, :year 1999, :extension "mp3",
                :genre "rock", :folder "/music/sfa/",
                :filename "asfd.mp3", :path "/music/sfa/asfd.mp3",
                :mod-date 2313, :album-id 2})

(facts 
  "about prep-songs"
  (fact
    "when passed a map of a raw-song it returns a coll containing a map of a song which ha been prepared for insetion into library"
    (prep-songs raw-song1 [album1 album2]) => [song1])
  (fact
    "when passed a coll containing two raw-songs in the same album it returns a coll containing a maps of  songs which have been prepared for insertion into library"
    (prep-songs [raw-song1 raw-song2] [album1 album2]) => [song1 song2])
  (fact
    "when passed a coll containing three raw-songs in two different albums it returns a coll containing a maps of  songs which have been prepared for insertion into library"
    (prep-songs [raw-song1 raw-song2 raw-song3] [album1 album2]) => [song1 song2 song3]))
