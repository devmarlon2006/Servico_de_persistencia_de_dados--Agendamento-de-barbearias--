package br.com.devmarlon2006.registrationbarberservice.Repository;

import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.BarberShop;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Table(name = "barbearias")
public interface BarberShopRepository extends JpaRepository<BarberShop, String> {

    boolean existsByPhone(String phone);

    boolean existsByAddress(String address);

    boolean existsByOwnerId(Barber ownerIdString);
}
