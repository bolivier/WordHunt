(ns wordhunt.events
  (:require [cljs.core.async :refer [<!] :refer-macros [go]]
            [re-frame.core :as rf]
            [wordhunt.db :as db]
            [wordhunt.dictionary :refer [lookup-word]]
            [wordhunt.subs :as subs]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-fx
 ::set-word-data
 (fn [cofx [_ word-data]]
   (let [language @(rf/subscribe [::subs/language])
         query    @(rf/subscribe [::subs/word-query])]
     {:db         (assoc (:db cofx) :word word-data)
      :dictionary {:language language
                   :query    query}})))

(rf/reg-fx
 :dictionary
 (fn [{:keys [query language]}]
   (when (and language query)
     (go
       (rf/dispatch [::set-word-data (js->clj (<! (lookup-word language query)))])))))
