(ns webdev.item.model
  (:require [clojure.java.jdbc :as db]))

(defn create-table [db]
  (db/execute! db
               ["CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\""])
  (db/execute! db
               ["CREATE TABLE IF NOT EXISTS items (
                 id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                 name TEXT NOT NULL,
                 description TEXT NOT NULL,
                 checked BOOLEAN NOT NULL DEFAULT FALSE,
                 date_created TIMESTAMP NOT NULL DEFAULT now()
               )"]))

(defn create-item [db name desc]
  (:id (first (db/query db ["INSERT INTO items (name, description) VALUES (?, ?) RETURNING id" name desc]))))

(defn update-item [db id checked]
  (= [1] (db/execute! db ["UPDATE items SET checked=? where id=?" checked id])))

(defn delete-item [db id]
  (= [1] (db/execute! db ["DELETE FROM items where id=?" id])))

(defn read-items [db]
(db/query db ["SELECT id, name, description, checked, date_created FROM items ORDER BY date_created"]))