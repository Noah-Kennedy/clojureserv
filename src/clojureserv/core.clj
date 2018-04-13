(ns clojureserv.core
  (:import (java.net Socket InetAddress ServerSocket)
           (java.io PrintWriter)
           (java.util Scanner)))

(require 'pyro.printer)

(pyro.printer/swap-stacktrace-engine!)

(defn ^Socket mk-client-socket [^String address,
                             ^Integer port]
  (Socket. (InetAddress/getByName address) port))

(defn ^PrintWriter mk-tcp-writer [^Socket socket]
  (-> socket
      .getOutputStream
      PrintWriter.))

(defn ^Scanner mk-tcp-reader [^Socket socket]
  (-> socket
      .getInputStream
      Scanner.))

(defn write-with-socket-agent [socket-agent,
                               ^String str]
  (send-off socket-agent #(do
                            (-> %
                               mk-tcp-writer
                               (.println str)
                                .flush)
                            %)))

(def server
  (-> (ServerSocket. 2401)
      agent))

(def client
  (-> (mk-client-socket "127.0.0.1" 2401)
      agent))

(send-off server #(.accept %))

(add-watch server :listener1 #(-> %4
                                  mk-tcp-reader
                                  .next
                                  println))