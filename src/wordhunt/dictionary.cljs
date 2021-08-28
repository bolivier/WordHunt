(ns wordhunt.dictionary
  (:require
   [cljs.core.async :refer [<! put! chan] :refer-macros [go]]
   [re-frame.core :as rf]))

(defn GET [url]
  (let [c (chan)]
    (-> (js/fetch url)
        (.then (fn [resp]
                 (.json resp)))
        (.then (fn [body]
                 (put! c body))))
    c))

(defn lookup-word [word]
  (GET
   "https://api.dictionaryapi.dev/api/v2/entries/en/plane"))
