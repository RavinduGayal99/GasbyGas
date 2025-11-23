package cw.gasbygas.service;

import cw.gasbygas.domain.UserStatus;
import cw.gasbygas.domain.UserType;
import cw.gasbygas.model.User;
import cw.gasbygas.request.BusinessRegistrationRequest;
import cw.gasbygas.request.CustomerRegistrationRequest;
import cw.gasbygas.request.LoginRequest;
import cw.gasbygas.request.OutletRegistrationRequest;
import cw.gasbygas.response.*;

import java.util.List;

public interface UserService
{
    User registerCustomer(CustomerRegistrationRequest request);
    User registerBusinessCustomer(BusinessRegistrationRequest request);
    User registerOutlet(OutletRegistrationRequest request);
    LoginResponse login(LoginRequest request) throws Exception;
    User updateUserStatus(int userId, UserStatus status);
    List<User> getUsersByType(UserType type);
    List<UserResponse> getUsersByStatusAndType(UserStatus status, String userType);
    List<DomesticUserResponse> getDomesticUsersByStatus(UserStatus status);
    List<BusinessUserResponse> getBusinessUsersByStatus(UserStatus status);
    List<OutletUserResponse> getOutletUsersByStatus(UserStatus status);

}
