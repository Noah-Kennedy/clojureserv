(ns clojureserv.core
  (:import (java.net Socket InetAddress)
           (java.io PrintWriter)
           (java.util Scanner)))

(defn mk-tcp-socket [name address port]
  (ref
    (Socket.
      (InetAddress/getByName address) port)))

(defn connect [socket address]
  (dosync
    (alter .connect socket address)))

(defn send-string [socket str]
  (dosync
    (->
      (deref socket)
      .getOutputStream
      PrintWriter.
      (.println str))))

(defn receive-string [socket]
  (dosync
    (->
      (deref socket)
      .getInputStream
      Scanner.
      .next)))