/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bibanon.anaunet;

import java.util.HashMap;

public class Users {

    HashMap<String, Rank> Users = new HashMap<String, Rank>();

    public void tmpImit() {
        Users.put("x404102", Rank.RE);
        Users.put("antonizoon", Rank.HOTEP);
        Users.put("Akaibu", Rank.VISITOR);
        Users.put("Gazlene", Rank.VISITOR);
        Users.put("r3c0d3x", Rank.HOTEP);
    }

    public void addUser(String user, Rank rank) {
        Users.put(user, rank);
    }

    public boolean isUser(String user) {
        if (Users.containsKey(user)) {
            return true;
        }
        return false;
    }

    public Rank getRank(String user) {
        Rank rank = Rank.DENIZEN;
        if (Users.containsKey(user)) {
            rank = Users.get(user);
        }
        return rank;
    }

    public boolean hasPermission(String user, String command) {
        Rank rank = getRank(user);
        return Rank.hasPermission(rank, command);
    }

    public enum Rank {
        RE,
        HOTEP,
        DENIZEN,
        VISITOR;

        private static boolean hasPermission(Rank rank, String command) {
            switch (rank) {
                case RE: {
                    return true;
                }
                case HOTEP: {
                    switch (command) {
                        case ".is": {
                            return true;
                        }
                        case ".grab": {
                            return true;
                        }
                        case ".rr": {
                            return true;
                        }
                        case ".time": {
                            return true;
                        }
                        default: {
                            return true;
                        }
                    }
                }
                case VISITOR: {
                    switch (command) {
                        case ".is": {
                            return true;
                        }
                        case ".grab": {
                            return true;
                        }
                        case ".rr": {
                            return true;
                        }
                        case ".time": {
                            return true;
                        }
                        default: {
                            return false;
                        }
                    }
                }
                case DENIZEN: {
                    switch (command) {
                        case ".time": {
                            return true;
                        }
                        default: {
                            return false;
                        }
                    }
                }
                default: {
                    switch (command) {
                        case ".time": {
                            return true;
                        }
                        default: {
                            return false;
                        }
                    }
                }
            }
        }
    }
}
