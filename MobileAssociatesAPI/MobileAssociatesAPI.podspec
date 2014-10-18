Pod::Spec.new do |spec|
  spec.name                   = 'MobileAssociatesAPI'
  spec.version                = '1.1.0'
  spec.summary                = 'Amazon Mobile Associates API'
  spec.public_header_files    = 'MobileAssociatesAPI.framework/**/*.h'
  spec.vendored_frameworks    = 'MobileAssociatesAPI.framework'
  spec.preserve_paths         = '*.framework'
  spec.frameworks             = 'AdSupport', 'CoreGraphics', 'UIKit', 'Foundation'
  spec.libraries              = 'sqlite3'
end

