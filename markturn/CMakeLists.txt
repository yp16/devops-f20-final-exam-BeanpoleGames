# Build for markturn

cmake_minimum_required(VERSION 3.18.4)

# project info # @NOTE Comment doesn't add anything
project(markturn VERSION "1.0.0")

option(LINK_STATIC "Build with static library" OFF)
message("LINK_STATIC: ${LINK_STATIC}")

find_package(CURL REQUIRED)
message("-- curl include: " ${CURL_INCLUDE_DIR})
message("-- curl library: " ${CURL_LIBRARY})

find_package(yaml-cpp REQUIRED)
message("-- yaml-cpp include: " ${YAML_CPP_INCLUDE_DIR})
message("-- yaml-cpp library: " ${YAML_CPP_LIBRARY})

find_package(LibXml2 REQUIRED)
message("-- libxml2 include: " ${LIBXML2_INCLUDE_DIR})
message("-- libxml2 library: " ${LIBXML2_LIBRARY})

find_package(jsoncpp REQUIRED)
message("-- jsoncpp include: " ${JSONCPP_INCLUDE_DIRS})
message("-- jsoncpp library: " ${JSONCPP_LIBRARIES})

# Build markturn
file(GLOB CLIENT_SOURCES "src/*.cpp")
add_executable(markturn ${CLIENT_SOURCES})
if(LINK_STATIC)
	message("Static library")
	target_link_libraries(markturn PUBLIC libmarkturn)
else()
	message("Shared library")
	target_link_libraries(markturn PUBLIC libmarkturn_shared)
endif()
target_include_directories(markturn PUBLIC src)

install(TARGETS markturn RUNTIME)

# @NOTE Why isn't an object library used?
# libraries
file(GLOB LIBRARY_SOURCES "src/*.cpp")

# @NOTE Use variable for long lists of duplicated libraries and include files
# static libmarkturn
add_library(libmarkturn STATIC ${LIBRARY_SOURCES})
target_include_directories(libmarkturn PUBLIC ${CURL_INCLUDE_DIR} ${YAML_CPP_INCLUDE_DIR} ${LIBXML2_INCLUDE_DIR} ${JSONCPP_INCLUDE_DIRS} /usr/include/yaml-cpp /usr/include/jsoncpp .)
target_link_libraries(libmarkturn PUBLIC ${CURL_LIBRARY} ${LIBXML2_LIBRARY} ${YAML_CPP_LIBRARY} ${JSONCPP_LIBRARIES})

install(TARGETS libmarkturn LIBRARY)  # @NOTE This is part of the proceeding paragraph

# shared libmarkturn library
add_library(libmarkturn_shared SHARED ${LIBRARY_SOURCES})
target_include_directories(libmarkturn_shared PUBLIC ${CURL_INCLUDE_DIR} ${YAML_CPP_INCLUDE_DIR} ${LIBXML2_INCLUDE_DIR} ${JSONCPP_INCLUDE_DIRS} /usr/include/yaml-cpp /usr/include/jsoncpp .)
target_link_libraries(libmarkturn_shared PUBLIC ${CURL_LIBRARY} ${LIBXML2_LIBRARY} ${YAML_CPP_LIBRARY} ${JSONCPP_LIBRARIES})
set_target_properties(libmarkturn_shared PROPERTIES OUTPUT_NAME libmarkturn)
set_target_properties(libmarkturn_shared PROPERTIES VERSION ${PROJECT_VERSION})
set_target_properties(libmarkturn_shared PROPERTIES SDVERSION ${PROJECT_VERSION_MAJOR})

install(TARGETS libmarkturn_shared LIBRARY) # @NOTE This is part of the proceeding paragraph

# Setup testing
enable_testing()

file(GLOB TEST_SOURCES "test/*.cpp")
foreach(TESTFILE ${TEST_SOURCES})
	get_filename_component(TEST_NAME ${TESTFILE} NAME_WLE)
	add_executable(${TEST_NAME} ${TESTFILE})
	target_link_libraries(${TEST_NAME} PUBLIC libmarkturn_shared)
	target_include_directories(${TEST_NAME} PUBLIC ${CMAKE_SOURCE_DIR} ${CMAKE_SOURCE_DIR}/src)
	add_test(NAME ${TEST_NAME} COMMAND ${TEST_NAME})
endforeach()

# Generate Simple Package
# @NOTE Keep comments consistent, even with capitalization, i.e., "Generate simple package"
set(CPACK_PACKAGE_NAME "markturnpack")
set(CPACK_PACKAGE_VERSION_MAJOR "1")
set(CPACK_PACKAGE_VERSION_MINOR "0")
set(CPACK_PACKAGE_VERSION_PATCH "0")
set(CPACK_PACKAGE_CONTACT "jpp52@zips.uakron.edu")
include(CPack)
# @NOTE Leave a single, blank line at the end of the file