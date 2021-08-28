(ns wordhunt.dictionary
  (:require
   [cljs.core.async :refer [<! put! chan] :refer-macros [go]]
   [re-frame.core :as rf]
   [clojure.string :as str]))

(defn GET [url]
  (let [c (chan)]
    (-> (js/fetch url)
        (.then (fn [resp]
                 (.json resp)))
        (.then (fn [body]
                 (put! c body))))
    c))

(defn lookup-word [language word]
  (GET
   (str/join "/"
             ["https://api.dictionaryapi.dev/api/v2/entries"
              language
              word])))

(def languages [{:value "English" :label "en"}
                {:value "Hindi" :label "hi"}
                {:value "Spanish" :label "es"}
                {:value "French" :label "fr"}
                {:value "Japanese" :label "ja"}
                {:value "Russian" :label "ru"}
                {:value "German" :label "de"}
                {:value "Italian" :label "it"}
                {:value "Korean" :label "ko"}
                {:value "Arabic" :label "ar"}])
