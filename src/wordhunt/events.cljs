(ns wordhunt.events
  (:require [cljs.core.async :refer [<!] :refer-macros [go]]
            [re-frame.core :as rf]
            [wordhunt.db :as db]
            [wordhunt.dictionary :refer [lookup-word]]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 ::set-word-data
 (fn [db [_ word-data]]
   (assoc db :word word-data)))

(rf/reg-fx
 :dictionary
 (fn [word]
   (go
     (rf/dispatch [::set-word-data (js->clj (<! (lookup-word word)))]))))

(rf/reg-event-fx
 ::get-word-info
 (fn [cofx [_ word]]
   {:dictionary word}))
