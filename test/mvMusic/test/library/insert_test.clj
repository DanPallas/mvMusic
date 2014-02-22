(ns mvMusic.test.library.insert-test
  (:use [midje.sweet]
        [mvMusic.library.insert])
  (:require [mvMusic.library.entities :as e]
            [mvMusic.library.fetch :as f]
            [mvMusic.library.util :as u]))

(facts 
  "about library/insert/albums"
  (fact "calls insert with the album entered when library is empty and returns truthy value"
        (albums {:title "title1" 
                 :artist "artist1" 
                 :image "image\\path1"
                 :sort "album sort"}) => truthy
        (provided 
          (f/albums) => []
          (insert e/albums '({:title "title1" 
                            :artist "artist1" 
                            :image "image\\path1"
                            :sort "album sort"})) => 1 :times 1))
  (fact "when called with album which isn't present in library, it is inserted and returns a truthy value"
        (albums {:title "title1" 
                 :artist "artist1" 
                 :image "image\\path1"
                 :sort "album sort"}) => truthy
        (provided 
          (f/albums) => [{:title "title2" 
                          :artist "artist1" 
                          :image "image\\path1"
                          :sort "album sort"}]
          (insert e/albums '({:title "title1" 
                            :artist "artist1" 
                            :image "image\\path1"
                            :sort "album sort"})) => 1 :times 1))
  (fact "when called with a single album which is already in library, it returns a nil value and doesn't call insert"
        (albums {:title "title1" 
                 :artist "artist1" 
                 :image "image\\path1"
                 :sort "album sort"}) => nil
        (provided
          (f/albums) => [{:title "title1" 
                                   :artist "artist1" 
                                   :image "image\\path1"
                                   :sort "album sort"}] 
          (insert e/albums anything) => anything :times 0))
  (fact "album art and id don't affect album equality"
        (albums {:title "title1" 
                 :artist "artist1" 
                 :image "image\\path1"
                 :sort "album sort"}) => nil
        (provided
          (f/albums) => [{:title "title1" 
                                   :artist "artist1" 
                                   :image "image\\path2"
                                   :sort "album sort"}]
          (insert e/albums) => anything :times 0))
  (fact "calls insert with all albums when called with multiple albums which do not exist in library."
        (albums [{:title "title1" 
                  :artist "artist1" 
                  :image "image\\path1"
                  :sort "album sort1"}
                 {:title "title2" 
                  :artist "artist1" 
                  :image "image\\path2"
                  :sort "album sort2"}
                 {:title "title1" 
                  :artist "artist2" 
                  :image "image\\path2"
                  :sort "album sort2"}]) => truthy
        (provided 
          (f/albums) => [{:title "title10" 
                                   :artist "artist2" 
                                   :image "image\\path2"
                                   :sort "album sort2"}]
          (insert e/albums 
                  (just
                    {:title "title1" 
                     :artist "artist1" 
                     :image "image\\path1"
                     :sort "album sort1"}
                    {:title "title2" 
                     :artist "artist1" 
                     :image "image\\path2"
                     :sort "album sort2"}
                    {:title "title1" 
                     :artist "artist2" 
                     :image "image\\path2"
                     :sort "album sort2"} :in-any-order)) => 1 :times 1))
  (fact "when called with multiple albums and some are in the library, it calls insert with all other albums"
        (albums [{:title "title1" 
                  :artist "artist1" 
                  :image "image\\path1"
                  :sort "album sort1"}
                 {:title "title2" 
                  :artist "artist1" 
                  :image "image\\path2"
                  :sort "album sort2"}
                 {:title "title1" 
                  :artist "artist2" 
                  :image "image\\path2"
                  :sort "album sort2"}]) => truthy
        (provided 
          (f/albums) => [{:title "title2" 
                                   :artist "artist1" 
                                   :image "image\\path2"
                                   :sort "album sort2"}]
          (insert e/albums 
                  (just
                    {:title "title1" 
                     :artist "artist1" 
                     :image "image\\path1"
                     :sort "album sort1"}
                    {:title "title1" 
                     :artist "artist2" 
                     :image "image\\path2"
                     :sort "album sort2"} :in-any-order)) => 1 :times 1))
  (fact "when called with a many albums which are already in library, it returns a nil value and doesn't call insert"
        (albums [{:title "title1" 
                  :artist "artist1" 
                  :image "image\\path1"
                  :sort "album sort"}
                 {:title "title2" 
                  :artist "artist1" 
                  :image "image\\path1"
                  :sort "album sort"} ]) => nil
        (provided
          (f/albums) => [{:title "title2" 
                                   :artist "artist1" 
                                   :image "image\\path1"
                                   :sort "album sort"}
                                  {:title "title1" 
                                   :artist "artist1" 
                                   :image "image\\path1"
                                   :sort "album sort"}] 
          (insert e/albums anything) => anything :times 0)))


(def album1 {:title "album1",
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
(def album2 {:title "album2",
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
  "about insert/songs"
  (fact "when called with a single map representing a song it adds the album, song, and album to the library with relationship."
        (songs raw-song1) => truthy
        (provided
          (f/albums) => [:album-list]
          (albums [album1]) => {:id 1}
          (u/prep-songs [raw-song1] [:album-list]) => [song1]
          (album-art [song1]) => true
          (insert e/songs [song1]) => {:id 1}))
  (fact "when called with a vector of maps representing songs in the same album it adds the album, songs, and album art to the library with relationships."
        (songs [raw-song1 raw-song2]) => truthy
        (provided
          (f/albums) => [:album-list]
          (albums [album1]) => {:id 1}
          (u/prep-songs (just [raw-song1 raw-song2] :in-any-order) 
                        [:album-list]) => [song1 song2] 
          (album-art (just [song1 song2] :in-any-order)) => true
          (insert e/songs (just [song1 song2] :in-any-order)) => {:id 1}))
  (fact "when called with a vector of maps representing songs in the different albums it adds the albums, songs, and album art to the library with relationships."
        (songs [raw-song1 raw-song2 raw-song3]) => truthy
        (provided
          (f/albums) => [:album-list]
          (albums (just [album1 album2] :in-any-order)) => {:id 2}
          (u/prep-songs 
            (just [raw-song1 raw-song2 raw-song3] :in-any-order) 
            [:album-list]) =>  [song1 song2 song3] 
          (album-art (just [song1 song2 song3] :in-any-order)) => true
          (insert e/songs (just [song1 song2 song3] :in-any-order)) => {:id 2})))
