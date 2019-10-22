package com.arman.springsecurityrest.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import javax.persistence.*

@Entity
@Table(name = "locations", schema = "public")
@JsonRootName("location")
data class Location(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(updatable = false, nullable = false, unique = true, insertable = false)
        var id: Long = 0,
        @Column(nullable = false)
        var name: String = ""
) {

    @ManyToMany(targetEntity = Device::class, mappedBy = "locations")
    @JsonIgnore
    val devices: MutableSet<Device> = hashSetOf()

    fun addDevice(device: Device) {
        this.devices.add(device)
        device.locations.add(this)
    }

    fun removeDevice(device: Device) {
        this.devices.add(device)
        device.locations.add(this)
    }

    override fun toString(): String = "location: { id: $id, name: $name }"

    @DslMarker
    private annotation class LocationBuilder

    @LocationBuilder
    data class Builder(
            var id: Long = 0,
            var name: String = ""
    ) {

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }

        fun build() = Location(id, name)

    }

}

inline fun location(buildLocation: Location.Builder.() -> Unit): Location {
    val builder = Location.Builder()
    builder.buildLocation()
    return builder.build()
}