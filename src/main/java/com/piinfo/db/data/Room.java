package com.piinfo.db.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "rooms")
@Getter @Setter
@RequiredArgsConstructor
public class Room {

    @Id
    @Column(nullable = false,updatable = false)
    private long id;

    @NonNull
    @Column
    public String name;

    public String getValue(){
        return String.format('<a href="%03d"></a>',);
    }

    @ManyToMany
    @JoinTable(joinColumns = {@JoinColumn(name = "Roomid",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "FurnitureId",referencedColumnName = "id")})
    private Set<Furniture> furniture;

}
