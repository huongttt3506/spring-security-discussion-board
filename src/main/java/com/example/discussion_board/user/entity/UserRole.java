package com.example.discussion_board.user.entity;

import java.util.Set;

public enum UserRole {
    ROLE_USER(Set.of(UserPermission.COMMENT, UserPermission.READ)),
    ROLE_AUTHOR(Set.of(UserPermission.WRITE, UserPermission.READ, UserPermission.COMMENT)),
    ROLE_ADMIN(Set.of(UserPermission.WRITE, UserPermission.READ, UserPermission.COMMENT, UserPermission.ADMIN)),
    ROLE_INACTIVE(Set.of(UserPermission.READ));


   private final Set<UserPermission> permissions;

   UserRole(Set<UserPermission> permissions) {
       this.permissions = permissions;
   }
   public Set<UserPermission> getPermissions() {
       return permissions;
   }
}
