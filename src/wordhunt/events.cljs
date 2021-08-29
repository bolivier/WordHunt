(ns wordhunt.events
  (:require [cljs.core.async :refer [<!] :refer-macros [go]]
            [re-frame.core :as rf]
            [wordhunt.db :as db]
            [wordhunt.dictionary :refer [lookup-word]]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :as csk]
            [wordhunt.subs :as subs]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 ::set-meanings
 (fn [db [_ meanings]]
   (assoc db ::meanings meanings)))

(rf/reg-sub
 ::subs/meanings
 (fn [db _]
   (::meanings db)))

(rf/reg-fx
 :dictionary
 (fn [{:keys [query language]}]
   (when (and language query)
     (go
       (rf/dispatch [::set-meanings (transform-keys
                                      csk/->kebab-case
                                      (js->clj (<! (lookup-word language query))
                                               :keywordize-keys true))])))))
