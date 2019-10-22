package com.arman.springsecurityrest.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import javax.persistence.*

@Entity
@Table(name = "devices", schema = "public")
@JsonRootName("device")
class Device(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(updatable = false, nullable = false, unique = true, insertable = false)
        var id: Long = 0,
        @ManyToOne(targetEntity = User::class)
        @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
        @JsonIgnore
        var user: User? = null,
        @Column(updatable = false, nullable = true)
        var details: String = ""
) {

    @ManyToMany(targetEntity = Location::class)
    @JoinTable(
            name = "device_locations",
            joinColumns = [JoinColumn(name = "device_id")],
            inverseJoinColumns = [JoinColumn(name = "location_id")]
    )
    @JsonIgnore
    val locations: MutableSet<Location> = hashSetOf()

    fun addLocation(location: Location) {
        this.locations.add(location)
        location.devices.add(this)
    }

    fun removeLocation(location: Location) {
        this.locations.remove(location)
        location.devices.remove(this)
    }

    override fun toString(): String = "device: { id: $id, details: $details }"

    @DslMarker
    private annotation class DeviceBuilder

    @DeviceBuilder
    data class Builder(
            var id: Long = 0,
            var user: User? = null,
            var details: String = ""
    ) {

        fun id(id: Long) = apply { this.id = id }
        fun user(user: User) = apply { this.user = user }
        fun details(details: String) = apply { this.details = details }

        fun build() = Device(id, user, details)

    }

}

inline fun device(buildDevice: Device.Builder.() -> Unit): Device {
    val builder = Device.Builder()
    builder.buildDevice()
    return builder.build()
}