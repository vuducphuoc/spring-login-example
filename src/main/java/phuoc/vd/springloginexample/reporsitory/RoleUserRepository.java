package phuoc.vd.springloginexample.reporsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phuoc.vd.springloginexample.entity.RoleUser;

import java.util.List;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Integer> {

    RoleUser findOneByUserId(Integer userId);

    List<RoleUser> findAllByUserId(Integer userId);

    List<RoleUser> findAllByRoleId(Integer roleId);

    List<RoleUser> findAllByUserIdAndRoleId(Integer userId, Integer roleId);
}
