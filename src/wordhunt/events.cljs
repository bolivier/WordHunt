(ns wordhunt.events
  (:require
   [re-frame.core :as rf]
   [wordhunt.db :as db]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
