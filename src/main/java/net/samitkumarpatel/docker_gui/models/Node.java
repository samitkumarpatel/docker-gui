package net.samitkumarpatel.docker_gui.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Node(
        @JsonProperty("ID") String id, @JsonProperty("CreatedAt") String createdAt, @JsonProperty("Spec") Spec spec, @JsonProperty("Status") Status status, @JsonProperty("ManagerStatus") ManagerStatus managerStatus) {
}
record Spec(@JsonProperty("Name") String name, @JsonProperty("Role") String role) {}
record Status( @JsonProperty("State") String state, @JsonProperty("Message") String message, @JsonProperty("Addr") String addr) {}
record ManagerStatus(@JsonProperty("Leader") String leader, @JsonProperty("Reachability") String reachability, @JsonProperty("Addr") String addr) {}