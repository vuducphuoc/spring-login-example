package phuoc.vd.springloginexample.reporsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phuoc.vd.springloginexample.entity.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role getByShortName(String shortName);

    List<Role> findAllByShortName(String shortName);
}
